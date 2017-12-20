package hu.unideb.inf.carrental.carimage.resource;

import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.commons.exception.enumeration.ExceptionType;
import hu.unideb.inf.carrental.commons.model.ErrorResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/car/image")
public class CarImageResource {
    private final CarImageService carImageService;

    @Autowired
    public CarImageResource(CarImageService carImageService) {
        this.carImageService = carImageService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam("image") MultipartFile[] files, @RequestParam("carId") long carId)
            throws NotFoundException, UnauthorizedAccessException {
        try {
            for (MultipartFile file : files) {
                carImageService.save(carId, file.getBytes());
            }
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new ErrorResponse(ExceptionType.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{id}/image")
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(carImageService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/id/{id}/image/jpeg", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getAsByteById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(carImageService.getAsByteById(id), HttpStatus.OK);
    }

    @GetMapping("/carId/{carId}/cover")
    public ResponseEntity<?> getCoverByCarId(@PathVariable("carId") long carId) throws NotFoundException {
        return new ResponseEntity<>(carImageService.getCoverByCarId(carId), HttpStatus.OK);
    }

    @GetMapping(value = "/carId/{carId}/cover/jpeg", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getCoverAsByteByCarId(@PathVariable("carId") long carId) throws NotFoundException {
        return new ResponseEntity<>(carImageService.getCoverAsByteByCarId(carId), HttpStatus.OK);
    }

    @GetMapping("/carId/{carId}/all")
    public ResponseEntity<?> getAllByCarId(@PathVariable("carId") long carId) {
        return new ResponseEntity<>(carImageService.getAllByCarId(carId), HttpStatus.OK);
    }

    @GetMapping("/carId/{carId}")
    public ResponseEntity<?> getIdsByCarId(@PathVariable("carId") long carId) {
        return new ResponseEntity<>(carImageService.getIdsByCarId(carId), HttpStatus.OK);
    }
}
