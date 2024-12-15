use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use regex::Regex;
use std::collections::HashSet;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "14"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

struct Robot {
    p: (i32, i32),
    v: (i32, i32),
}

const TEST: &str = "\
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3";

fn main() -> Result<()> {
    start_day(DAY);

    fn parse<R: BufRead>(mut reader: R) -> Vec<Robot> {
        let regex = Regex::new(r"(?m)p=(\d{1,3}),(\d{1,3}) v=(-?\d{1,3}),(-?\d{1,3})").unwrap();
        let mut text = String::new();
        reader
            .by_ref()
            .read_to_string(&mut text)
            .expect("TODO: panic message");
        let robots = regex
            .captures_iter(&text)
            .map(|caps| {
                let (_, [p_x, p_y, v_x, v_y]) = caps.extract();
                let p = (p_x.parse::<i32>().unwrap(), p_y.parse::<i32>().unwrap());
                let v = (v_x.parse::<i32>().unwrap(), v_y.parse::<i32>().unwrap());
                Robot { p, v }
            })
            .collect_vec();
        robots
    }

    //region Part 1
    println!("=== Part 1 ===");
    fn part1<R: BufRead>(reader: R, xmax: i32, ymax: i32) -> Result<usize> {
        let robots = parse(reader);

        let end_pos = robots
            .iter()
            .map(|r| {
                let x = r.p.0 + 100 * r.v.0;
                let y = r.p.1 + 100 * r.v.1;
                (x.rem_euclid(xmax), y.rem_euclid(ymax))
            })
            .collect_vec();

        let mut quadrant = [0; 4];
        let xmid = (xmax - 1) / 2;
        let ymid = (ymax - 1) / 2;
        for (x, y) in end_pos {
            if x < xmid && y < ymid {
                quadrant[0] += 1;
            }
            if x > xmid && y < ymid {
                quadrant[1] += 1;
            }
            if x < xmid && y > ymid {
                quadrant[2] += 1;
            }
            if x > xmid && y > ymid {
                quadrant[3] += 1;
            }
        }

        let answer = quadrant[0] * quadrant[1] * quadrant[2] * quadrant[3];
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(12, part1(BufReader::new(TEST.as_bytes()), 11, 7)?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file, 101, 103)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let robots = parse(reader);
        let num_bots = robots.len();
        let mut seconds = 0;
        loop {
            let end_pos: HashSet<(i32, i32)> = robots
                .iter()
                .map(|r| {
                    let x = r.p.0 + seconds * r.v.0;
                    let y = r.p.1 + seconds * r.v.1;
                    (x.rem_euclid(101), y.rem_euclid(103))
                })
                .collect();

            if end_pos.len() == num_bots {
                break;
            }

            seconds += 1;
        }

        Ok(seconds as usize)
    }

    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
